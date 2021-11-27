/*
 * This file is generated by jOOQ.
 */
package net.ttcxy.chat.db;


import net.ttcxy.chat.db.tables.Group;
import net.ttcxy.chat.db.tables.GroupMember;
import net.ttcxy.chat.db.tables.Member;
import net.ttcxy.chat.db.tables.Message;


/**
 * Convenience access to all tables in chat
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>chat.group</code>.
     */
    public static final Group GROUP = Group.GROUP;

    /**
     * The table <code>chat.group_member</code>.
     */
    public static final GroupMember GROUP_MEMBER = GroupMember.GROUP_MEMBER;

    /**
     * The table <code>chat.member</code>.
     */
    public static final Member MEMBER = Member.MEMBER;

    /**
     * The table <code>chat.message</code>.
     */
    public static final Message MESSAGE = Message.MESSAGE;
}
