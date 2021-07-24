/*
 * Copyright 2021 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package com.factory.deepforestrunner.rest.runner;

import com.factory.deepforestrunner.entity.Participant;
import com.factory.deepforestrunner.entity.Subdivision;
import com.factory.deepforestrunner.entity.dto.RunnerDTO;
import com.factory.deepforestrunner.service.ParticipantService;
import com.factory.deepforestrunner.service.RunnerService;
import com.factory.deepforestrunner.service.SubdivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.factory.deepforestrunner.rest.runner.util.RunnerUtil.runner_2_dto;

/**
 * RunnerController data
 *
 * @author <a href="mailto:Anatoly.Glazkov@russianpost.ru">Anatoly Glazkov</a> on 24.07.2021.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/runner")
public class RunnerController {

    private final RunnerService runnerService;
    private final SubdivisionService subdivisionService;
    private final ParticipantService participantService;

    @GetMapping
    public String list(
        final Model model
    ) {

        final Map<Long, Subdivision> subdivisionMap = subdivisionService.list().stream()
            .collect(Collectors.toMap(Subdivision::getId, Function.identity()));

        final Map<Long, Participant> participantMap = participantService.list().stream()
            .collect(Collectors.toMap(Participant::getId, Function.identity()));

        final List<RunnerDTO> runners = runnerService.list().stream()
            .map(runner -> runner_2_dto(
                runner,
                participantMap.getOrDefault(runner.getParticipantId(), new Participant()),
                subdivisionMap.getOrDefault(runner.getSubdivisionId(), new Subdivision()))
            )
            .collect(Collectors.toList());

        model.addAttribute("runners", runners);
        return "runner";
    }
}