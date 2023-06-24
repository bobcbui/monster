package net.ttcxy.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMemberMessage;

public interface MemberMessageRepository extends CrudRepository<CtsMemberMessage,String>  {

    @Query(value = "select * from cts_member_message where account = ?1 and with_account = ?2 order by create_time",nativeQuery = true)
    List<CtsMemberMessage> findByAccountAndWithAccount(String account, String string);

}
