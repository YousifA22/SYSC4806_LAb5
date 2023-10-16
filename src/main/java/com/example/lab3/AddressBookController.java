package com.example.lab3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.Map;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private BuddyRepository buddyInfoRepository;

    @PostMapping("/create")
    public AddressBook createAddressBook() {
        AddressBook addressBook = new AddressBook();
        return addressBookRepository.save(addressBook);
    }

    @PostMapping("/{addressBookId}/addbuddy")
    public AddressBook addBuddy(@PathVariable Long addressBookId, @RequestBody Map<String, String> body, Model model) {
        String name = body.get("name");
        model.addAttribute("Name",name);
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new RuntimeException("AddressBook not found"));
        BuddyInfo buddyInfo = new BuddyInfo(name);
        buddyInfo = buddyInfoRepository.save(buddyInfo);
        addressBook.addBuddy(buddyInfo);
        return addressBookRepository.save(addressBook);
    }

    @DeleteMapping("/{addressBookId}/removebuddy/{buddyId}")
    public AddressBook removeBuddy(@PathVariable Long addressBookId, @PathVariable Long buddyId) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new RuntimeException("AddressBook not found"));
        BuddyInfo buddyInfo = buddyInfoRepository.findById(buddyId)
                .orElseThrow(() -> new RuntimeException("BuddyInfo not found"));
        addressBook.removeBuddy(buddyInfo);
        return addressBookRepository.save(addressBook);
    }

    @GetMapping("/{addressBookId}/buddies")
    public Iterable<BuddyInfo> listBuddies(@PathVariable Long addressBookId,Model model) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new RuntimeException("AddressBook not found"));
        model.addAttribute(addressBook.getBuddies());
        return addressBook.getBuddies();
    }

}