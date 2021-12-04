package net.ttcxy.chat.dao;

import cn.hutool.core.date.DateTime;

public interface MemberDao {

    int selectIdByUsernameMember(String username);

    DateTime selectDataTimeById(String id);
}