package com.thoughtworks.i1.emailSender.service.model;

import com.thoughtworks.i1.emailSender.domain.Address;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {
    @Test
    public void test_should_use_email_address_as_user_name_when_user_name_not_provided(){
        Address address = Address.anAddress("i1.email@b.com");
        assertThat(address.getUserName(), is("i1.email"));
    }
}
