package com.pimp.commons.mongo;

import java.io.InputStream;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.pimp.commons.exceptions.SpringBeanCreationException;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author Kevin Goy
 */
public class MongoFileStorage extends PimpSpringBean implements IFileStorage {
  private GridFsOperations gridOperations;
  private boolean startEmpty = false;

  public MongoFileStorage(GridFsOperations gridOperations) {
    this.gridOperations = gridOperations;
  }

  protected void postConstruct() throws SpringBeanCreationException {
    super.postConstruct();
    if(this.startEmpty) {
      this.gridOperations.delete(new Query());
    }

  }

  public void write(String name, InputStream contentStream) {
    this.gridOperations.store(contentStream, name);
  }

  public InputStream read(String name) {
    GridFSDBFile gridFSDBFile = this.gridOperations.findOne(this.getFilenameQuery(name));
    return gridFSDBFile != null?gridFSDBFile.getInputStream():null;
  }

  public void delete(String name) {
    this.gridOperations.delete(this.getFilenameQuery(name));
  }

  private Query getFilenameQuery(String name) {
    return Query.query(Criteria.where("filename").is(name));
  }

  public void setStartEmpty(boolean startEmpty) {
    this.startEmpty = startEmpty;
  }
}
