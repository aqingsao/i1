package com.thoughtworks.i1.emailSender.service.model;

import com.thoughtworks.i1.emailSender.domain.Address;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {
    @Test
    public void test_should_use_email_address_as_user_name_when_user_name_not_provided() {
        assertThat(Address.anAddress("i1.email@b.com").getUserName(), is("i1.email"));
    }

    @Test
    public void should_return_true_when_address_is_valid() {
        assertThat(Address.anAddress("a@b.com").isValid(), is(true));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_1() {
        assertThat(Address.anAddress("@b.com").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_2() {
        assertThat(Address.anAddress("a@.com").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_3() {
        assertThat(Address.anAddress("@.com").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_4() {
        assertThat(Address.anAddress("ab.com").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_5() {
        assertThat(Address.anAddress("a.b@.com").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_6() {
        assertThat(Address.anAddress("a@b.com.").isValid(), is(false));
    }

    @Test
    public void should_return_false_when_address_is_not_valid_7() {
        assertThat(Address.anAddress("a@b.c").isValid(), is(false));
    }
}
