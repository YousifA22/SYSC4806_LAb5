package com.example.lab3;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuddyRepository extends CrudRepository<BuddyInfo, Long> {

    List<BuddyInfo> findByName(String Name);

    BuddyInfo findById(long id);
    //
}
