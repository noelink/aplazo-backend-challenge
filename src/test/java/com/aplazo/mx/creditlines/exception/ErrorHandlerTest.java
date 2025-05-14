package com.aplazo.mx.creditlines.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import  org.springframework.http.HttpHeaders;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleCustomerCreditException_returnsExpectedResponse() {
        // Arrange
        CustomerCreditException ex = new CustomerCreditException("Internal error", 100);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/test");

        WebRequest webRequest = mock(WebRequest.class);

        // Act
        ResponseEntity<ErrorResponse> response = errorHandler.handleCustomerCreditException(ex, webRequest, servletRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleIllegalUUID_returnsExpectedResponse() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Invalid UUID");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/uuid");

        WebRequest webRequest = mock(WebRequest.class);

        // Act
        ResponseEntity<ErrorResponse> response = errorHandler.handleIllegalUUID(ex, webRequest, servletRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid UUID", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/api/uuid", response.getBody().getPath());
    }

    @Test
    void handleHttpMessageNotReadable_returnsExpectedResponse() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Malformed JSON request");
        HttpHeaders headers = new HttpHeaders();
        MockWebRequest request = new MockWebRequest("/api/malformed");

        // Act
        ResponseEntity<Object> response = errorHandler.handleHttpMessageNotReadable(ex, headers, HttpStatus.BAD_REQUEST, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Malformed JSON request", error.getMessage());
        assertEquals("/api/malformed", error.getPath());
    }

    // MockWebRequest for testing purposes
    static class MockWebRequest implements WebRequest {
        private final String contextPath;

        public MockWebRequest(String contextPath) {
            this.contextPath = contextPath;
        }

        @Override
        public String getContextPath() {
            return contextPath;
        }

        // Métodos no usados en los tests pueden dejarse vacíos
        @Override public Object getAttribute(String name, int scope) { return null; }
        @Override public void setAttribute(String name, Object value, int scope) {}
        @Override public void removeAttribute(String name, int scope) {}
        @Override public String[] getAttributeNames(int scope) { return new String[0]; }

        @Override
        public void registerDestructionCallback(String name, Runnable callback, int scope) {

        }

        @Override
        public Object resolveReference(String key) {
            return null;
        }

        @Override
        public String getSessionId() {
            return "";
        }

        @Override
        public Object getSessionMutex() {
            return null;
        }

        @Override public String getParameter(String paramName) { return null; }
        @Override public String[] getParameterValues(String paramName) { return new String[0]; }

        @Override
        public Iterator<String> getParameterNames() {
            return null;
        }

        @Override public Map<String, String[]> getParameterMap() { return null; }
        @Override public Locale getLocale() { return null; }
        @Override public String getHeader(String headerName) { return null; }
        @Override public String[] getHeaderValues(String headerName) { return new String[0]; }
        @Override public Iterator<String> getHeaderNames() { return null; }
        @Override public String getRemoteUser() { return null; }
        @Override public Principal getUserPrincipal() { return null; }
        @Override public boolean isUserInRole(String role) { return false; }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override public boolean checkNotModified(long lastModifiedTimestamp) { return false; }
        @Override public boolean checkNotModified(String etag) { return false; }
        @Override public boolean checkNotModified(String etag, long lastModifiedTimestamp) { return false; }
        @Override public String getDescription(boolean includeClientInfo) { return null; }

    }
}
