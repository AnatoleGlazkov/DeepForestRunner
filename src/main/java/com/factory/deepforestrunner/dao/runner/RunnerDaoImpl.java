/*
 * Copyright 2021 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package com.factory.deepforestrunner.dao.runner;

import com.factory.deepforestrunner.dao.RunnerDao;
import com.factory.deepforestrunner.dao.runner.rowmapper.RunnerRowMapper;
import com.factory.deepforestrunner.entity.model.Participant;
import com.factory.deepforestrunner.entity.model.Runner;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

import static com.factory.deepforestrunner.util.SqlUtil.SQL_long;

/**
 * RunnerDaoImpl data
 *
 * @author <a href="mailto:Anatoly.Glazkov@russianpost.ru">Anatoly Glazkov</a> on 24.07.2021.
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class RunnerDaoImpl implements RunnerDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void clearAll() {
        jdbcTemplate.update("DELETE FROM runner");
    }

    @Override
    public List<Runner> list() {
        return jdbcTemplate.query(
            "SELECT * FROM runner",
            new RunnerRowMapper());
    }

    @Override
    public void createAll(
        final List<Participant> runners
    ) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO runner (participant_id) " +
                "VALUES(?) ON CONFLICT DO NOTHING",
            new BatchPreparedStatementSetter() {

                public void setValues(
                    final PreparedStatement ps,
                    final int i
                ) {
                    SQL_long(ps, 1, runners.get(i).getId());
                }

                public int getBatchSize() {
                    return runners.size();
                }
            });
    }
}