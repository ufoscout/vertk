package com.ufoscout.vertk.filesystem

import io.vertx.core.buffer.Buffer
import io.vertx.core.file.AsyncFile
import io.vertx.core.file.CopyOptions
import io.vertx.core.file.FileSystem
import io.vertx.core.file.OpenOptions
import io.vertx.kotlin.coroutines.awaitResult


inline suspend fun FileSystem.awaitCopy(from: String, to: String) {
    awaitResult<Void> {
        this.copy(from, to, it)
    }
}

inline suspend fun FileSystem.awaitCopy(from: String, to: String, options: CopyOptions) {
    awaitResult<Void> {
        this.copy(from, to, options, it)
    }
}


inline suspend fun FileSystem.awaitCopyRecursive(from: String, to: String, recursive: Boolean) {
    awaitResult<Void> {
        this.copyRecursive(from, to, recursive, it)
    }
}

inline suspend fun FileSystem.awaitMove(from: String, to: String) {
    awaitResult<Void> {
        this.move(from, to, it)
    }
}

inline suspend fun FileSystem.awaitMove(from: String, to: String, options: CopyOptions) {
    awaitResult<Void> {
        this.move(from, to, options, it)
    }
}

inline suspend fun FileSystem.awaitDelete(path: String) {
    awaitResult<Void> {
        this.delete(path, it)
    }
}

inline suspend fun FileSystem.awaitDeleteRecursive(path: String, recursive: Boolean) {
    awaitResult<Void> {
        this.deleteRecursive(path, recursive, it)
    }
}

inline suspend fun FileSystem.awaitMkdir(path: String) {
    awaitResult<Void> {
        this.mkdir(path, it)
    }
}

inline suspend fun FileSystem.awaitMkdirs(path: String) {
    awaitResult<Void> {
        this.mkdirs(path, it)
    }
}

inline suspend fun FileSystem.awaitReadDir(path: String): List<String> {
    return awaitResult<List<String>> {
        this.readDir(path, it)
    }
}

inline suspend fun FileSystem.awaitReadDir(path: String, filter: String): List<String> {
    return awaitResult<List<String>> {
        this.readDir(path, filter, it)
    }
}

inline suspend fun FileSystem.awaitCreateFile(path: String) {
    awaitResult<Void> {
        this.createFile(path, it)
    }
}

inline suspend fun FileSystem.awaitOpen(path: String, options: OpenOptions): AsyncFile {
    return awaitResult<AsyncFile> {
        this.open(path, options, it)
    }
}

inline suspend fun FileSystem.awaitExists(path: String): Boolean {
    return awaitResult<Boolean> {
        this.exists(path, it)
    }
}

inline suspend fun FileSystem.awaitReadFile(path: String): Buffer {
    return awaitResult<Buffer> {
        this.readFile(path, it)
    }
}


inline suspend fun FileSystem.awaitWriteFile(path: String, data: Buffer) {
    awaitResult<Void> {
        this.writeFile(path, data, it)
    }
}