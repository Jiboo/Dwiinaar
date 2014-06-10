package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MuLinks {

    public class Entry {
        Rect rect;
    }

    public class EntryGoto extends Entry {
        int page, flags;
        String dest, file_spec;
        Point lt, rb;
        boolean new_window;
    }

    public class EntryUri extends Entry {
        Uri uri;
        boolean is_map;
    }

    public class EntryLaunch extends Entry {
        String file_spec;
        boolean new_window, is_uri;
    }

    public class EntryNamed extends Entry {
        String named;
    }

    protected long dNativePointer;
    protected long dNativeDocument;
    protected long dNativeContext;

    protected List<Entry> dEntries = new ArrayList<>();

    protected native static void nFeedAndFree(long ctx, long links, List<Entry> dst);

    protected MuLinks(long ctx, long nativePointer) {
        nFeedAndFree(ctx, nativePointer, dEntries);
    }

    public List<Entry> getEntries() {
        return dEntries;
    }
}
