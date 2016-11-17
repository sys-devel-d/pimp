package com.pimp.domain;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Theories.class)
public class ClientDocumentTest {

    @DataPoint
    public static List<String> data = Arrays.asList("foo", "bar", "baz");

    @Theory
    public void testResourceIdsAsString(List<String> data) throws Exception {
        ClientDocument clientDocument = new ClientDocument().setResourceIds(data);
        assertThat(clientDocument.resourceIdsAsString()).isEqualTo("foo,bar,baz");
    }

    @Theory
    public void testGrantTypesAsString(List<String> data) throws Exception {
        ClientDocument clientDocument = new ClientDocument().setGrantTypes(data);
        assertThat(clientDocument.grantTypesAsString()).isEqualTo("foo,bar,baz");
    }

    @Theory
    public void testAuthoritiesAsString(List<String> data) throws Exception {
        ClientDocument clientDocument = new ClientDocument().setAuthorities(data);
        assertThat(clientDocument.authoritiesAsString()).isEqualTo("foo,bar,baz");
    }

    @Theory
    public void testScopesAsString(List<String> data) throws Exception {
        ClientDocument clientDocument = new ClientDocument().setScopes(data);
        assertThat(clientDocument.scopesAsString()).isEqualTo("foo,bar,baz");
    }
}