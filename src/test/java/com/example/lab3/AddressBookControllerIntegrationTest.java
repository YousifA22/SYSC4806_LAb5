package com.example.lab3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressBookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createAddressBook_shouldReturnAddressBook() {
        ResponseEntity<AddressBook> responseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/api/addressbook/create", null, AddressBook.class);

        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void addBuddy_shouldAddBuddyToAddressBook() {
        // First create an AddressBook
        AddressBook addressBook = restTemplate.postForObject("http://localhost:" + port + "/api/addressbook/create", null, AddressBook.class);
        Long addressBookId = addressBook.getId();

        // Construct the BuddyInfo object
        BuddyInfo buddy = new BuddyInfo("John Doe");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/addbuddy", buddy, AddressBook.class);

        // Verify
        ResponseEntity<BuddyInfo[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/buddies", BuddyInfo[].class);
        BuddyInfo[] buddies = responseEntity.getBody();

        assertThat(buddies).isNotEmpty();
        assertThat(buddies[0].getName()).isEqualTo("John Doe");
    }

    @Test
    public void removeBuddy_shouldRemoveBuddyFromAddressBook() {
        // First create an AddressBook
        AddressBook addressBook = restTemplate.postForObject("http://localhost:" + port + "/api/addressbook/create", null, AddressBook.class);
        Long addressBookId = addressBook.getId();

        // Add a buddy to the address book
        BuddyInfo addedBuddy = new BuddyInfo("Jane Doe");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/addbuddy", addedBuddy, AddressBook.class);

        // Verify buddy was added
        ResponseEntity<BuddyInfo[]> responseBeforeDelete = restTemplate.getForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/buddies", BuddyInfo[].class);
        assertThat(responseBeforeDelete.getBody()).isNotEmpty();

        // Now remove the buddy
        Long buddyId = responseBeforeDelete.getBody()[0].getId();
        restTemplate.delete("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/removebuddy/" + buddyId);

        // Verify buddy was removed
        ResponseEntity<BuddyInfo[]> responseAfterDelete = restTemplate.getForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/buddies", BuddyInfo[].class);
        assertThat(responseAfterDelete.getBody()).isEmpty();
    }

    @Test
    public void listBuddies_shouldListAllBuddiesOfAddressBook() {
        // First create an AddressBook
        AddressBook addressBook = restTemplate.postForObject("http://localhost:" + port + "/api/addressbook/create", null, AddressBook.class);
        Long addressBookId = addressBook.getId();

        // Add two buddies to the address book
        BuddyInfo buddy1 = new BuddyInfo("Alice");
        BuddyInfo buddy2 = new BuddyInfo("Bob");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/addbuddy", buddy1, AddressBook.class);
        restTemplate.postForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/addbuddy", buddy2, AddressBook.class);

        // List buddies
        ResponseEntity<BuddyInfo[]> response = restTemplate.getForEntity("http://localhost:" + port + "/api/addressbook/" + addressBookId + "/buddies", BuddyInfo[].class);
        BuddyInfo[] buddies = response.getBody();

        assertThat(buddies).hasSize(2);
        assertThat(buddies[0].getName()).isEqualTo("Alice");
        assertThat(buddies[1].getName()).isEqualTo("Bob");
    }


}

