package com.pimp.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomOAuth2RequestFactoryTest {

  @Test
  public void testMatches() throws Exception {
    assertThat(CustomOAuth2RequestFactory.matchesScope("USER", "user_actions")).isTrue();
  }
}
