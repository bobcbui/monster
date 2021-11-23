/*
 * This file is generated by jOOQ.
 */
package net.ttcxy.chat.db.tables;


import java.time.LocalDateTime;

import net.ttcxy.chat.db.Chat;
import net.ttcxy.chat.db.tables.records.GroupRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Group extends TableImpl<GroupRecord> {

    private static final long serialVersionUID = -1006057699;

    /**
     * The reference instance of <code>chat.group</code>
     */
    public static final Group GROUP = new Group();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GroupRecord> getRecordType() {
        return GroupRecord.class;
    }

    /**
     * The column <code>chat.group.id</code>.
     */
    public final TableField<GroupRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>chat.group.name</code>.
     */
    public final TableField<GroupRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>chat.group.create_time</code>.
     */
    public final TableField<GroupRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * Create a <code>chat.group</code> table reference
     */
    public Group() {
        this(DSL.name("group"), null);
    }

    /**
     * Create an aliased <code>chat.group</code> table reference
     */
    public Group(String alias) {
        this(DSL.name(alias), GROUP);
    }

    /**
     * Create an aliased <code>chat.group</code> table reference
     */
    public Group(Name alias) {
        this(alias, GROUP);
    }

    private Group(Name alias, Table<GroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private Group(Name alias, Table<GroupRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Group(Table<O> child, ForeignKey<O, GroupRecord> key) {
        super(child, key, GROUP);
    }

    @Override
    public Schema getSchema() {
        return Chat.CHAT;
    }

    @Override
    public Group as(String alias) {
        return new Group(DSL.name(alias), this);
    }

    @Override
    public Group as(Name alias) {
        return new Group(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Group rename(String name) {
        return new Group(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Group rename(Name name) {
        return new Group(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, LocalDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
