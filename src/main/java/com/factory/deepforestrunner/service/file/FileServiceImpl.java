/*
 * Copyright 2021 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package com.factory.deepforestrunner.service.file;

import com.factory.deepforestrunner.dao.FileDao;
import com.factory.deepforestrunner.entity.Participant;
import com.factory.deepforestrunner.service.FileService;
import com.factory.deepforestrunner.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileServiceImpl data
 *
 * @author <a href="mailto:Anatoly.Glazkov@russianpost.ru">Anatoly Glazkov</a> on 21.07.2021.
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;

    @Override
    public void create(
        final MultipartFile file
    ) {
        try {

            fileDao.create(file);

            final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(file.getBytes());
            final Workbook workbook = WorkbookFactory.create(arrayInputStream);

            // subdivision 0
            // participant 1

            final Sheet sheet = workbook.getSheetAt(1);

            final List<Participant> participants = new ArrayList<>();

            final DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                participants.add(
                    ParseUtil.parseParticipant(
                        sheet.getRow(i),
                        formatter
                    )
                );
            }

            arrayInputStream.close();
        } catch (IOException | InvalidFormatException ioException) {
            ioException.printStackTrace();
        }
    }
}