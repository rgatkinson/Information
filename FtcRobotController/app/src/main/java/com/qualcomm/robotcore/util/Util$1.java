package com.qualcomm.robotcore.util;

import java.io.File;
import java.util.Comparator;

static final class Util$1 implements Comparator<File> {
    public int a(final File file, final File file2) {
        return file.getName().compareTo(file2.getName());
    }
}