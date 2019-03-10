package com.kara4k.spiders.pipeline.impl.simple.downloader.holder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.FileName;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.FileSource;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.SaveFolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // TODO: 09.03.2019 clear
@NoArgsConstructor
@DatabaseTable(tableName = "Test")
public class FileHolder {
    @DatabaseField(generatedId = true)
    private long id;
    @FileName
    @DatabaseField
    private String fileName;
    @FileSource
    @DatabaseField
    private String fileSource;
    @SaveFolder
    @DatabaseField
    private String saveFolder;
}
