package com.github.lucascalheiros.fuzzysearch

class FuzzySearch {

    /**
     * A native method that is implemented by the 'fuzzysearch' native library,
     * which is packaged with this application.
     */
    external fun levenshtein(text: String, query: String): Int

    companion object {
        // Used to load the 'fuzzysearch' library on application startup.
        init {
            System.loadLibrary("fuzzysearch")
        }
    }
}