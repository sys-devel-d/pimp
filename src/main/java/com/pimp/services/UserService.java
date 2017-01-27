package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.mongo.MongoFileStorage;
import com.pimp.domain.User;
import com.pimp.domain.UserDocument;
import com.pimp.repositories.UserRepository;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

  private UserRepository userRepository;
  private MongoOperations mongoOperations;
  private MongoFileStorage fileStorage;
  private NotificationDispatcherService notificationService;
  private CalendarService calendarService;
  private BCryptPasswordEncoder encoder;

  @Autowired
  public UserService(UserRepository userRepository,
                     MongoOperations mongoOperations,
                     MongoFileStorage fileStorage,
                     NotificationDispatcherService notificationService,
                     CalendarService calendarService,
                     BCryptPasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.mongoOperations = mongoOperations;
    this.fileStorage = fileStorage;
    this.notificationService = notificationService;
    this.calendarService = calendarService;
    this.encoder = encoder;
  }

  public UserDocument createUser(User user) {
    String userName = user.getUserName();
    if (existsWithUsername(userName)) {
      throw new EntityAlreadyExistsException("User already exists with username: " + userName);
    }
    String email = user.getEmail();
    if (existsWithEmail(email)) {
      throw new EntityAlreadyExistsException("User already exists with email: " + email);
    }

    UserDocument userDocument = UserDocument.from(user)
            .setPassword(encoder.encode(user.getPassword()));
    if (userDocument.getRoles().isEmpty()) {
      userDocument.setRoles(Arrays.asList("ROLE_USER"));
    }
    userRepository.save(userDocument);
    notificationService.create(userName);
    calendarService.createPrivateCalendar(userName);

    return userDocument;
  }

  private boolean existsWithUsername(String userName) {
    return userRepository.findByUserName(userName) != null;
  }

  private boolean existsWithEmail(String email) {
    return userRepository.findByEmail(email) != null;
  }

  public User findByUserName(String username) {
    UserDocument userDocument = userRepository.findByUserName(username);

    if (userDocument == null) {
      throw new EntityNotFoundException("Unable to find user " + username);
    }

    return User.from(userDocument);
  }

  public List<User> findAll() {
    return userRepository.findAll().stream().map(User::from).collect(Collectors.toList());
  }

  public void save(User user) {
    userRepository.save(UserDocument.from(user));
  }

  public List<User> query(String query, List<String> queryParameter) {
    Query mongoQuery = new Query();
    Criteria criteria = new Criteria();
    Criteria[] criterias = queryParameter.stream()
            .map(param -> Criteria.where(param).regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE)))
            .toArray(Criteria[]::new);
    criteria.orOperator(criterias);
    mongoQuery.addCriteria(criteria);
    return mongoOperations.find(mongoQuery, UserDocument.class)
            .stream().map(User::from).collect(Collectors.toList());
  }

  public boolean exists(String userName) {
      return userRepository.exists(userName);
  }

  public String uploadPhoto(String userKey, String file) throws IOException {
    User user = this.findByUserName(userKey);
    if (user.getPhoto() != null) {
      fileStorage.delete(user.getPhoto());
    }

    ObjectId objectId = new ObjectId();
    InputStream is = new ByteArrayInputStream(file.getBytes());
    fileStorage.write(objectId.toString(), is);

    user.setPhoto(objectId.toString());

    this.save(user);

    return objectId.toString();
  }

  public String findPhotoByName(String name) throws IOException {
    InputStream inputStream = fileStorage.read(name);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, byteArrayOutputStream);
    return String.valueOf(byteArrayOutputStream);
  }
}
