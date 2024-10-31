package com.zedeck.ipftestscenario.repositories;


import com.zedeck.ipftestscenario.models.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findFirstByUsername(String username);


    Optional<UserAccount> findFirstByUuid(String uuid);



}
