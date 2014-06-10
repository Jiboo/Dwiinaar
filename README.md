Dwiinaar
========

Hobby comics reader with some reusable libraries/components, under the AGPLv3 lisence (see COPYING).
  * BitmapManager: Manages bitmaps for asynch loading, caching, and transparent reusing when available.
    * BitmapBin: Portable way to alloc/free bitmaps, through a claim/offer interface.
      * For version lower than 11, it will only create and recycle bitmaps.
      * For version 11 to 18, it try to reuse a previously offered bitmap with the same size/configuration.
      * For version 19 and up, it try to reuse a suitable previously offered bitmap and reconfigure it.
    * BitmapCache: LruCache of bitmaps and asynchronous decode using a threadpool.
  * MuPDF: Android bindings for MuPDF rendering API.