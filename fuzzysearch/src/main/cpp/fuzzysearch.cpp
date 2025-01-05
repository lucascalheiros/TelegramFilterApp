#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include "string_utils.h"

using namespace std;

// Credits to https://www.geeksforgeeks.org/introduction-to-levenshtein-distance/
int levenshteinTwoMatrixRows(const string &str1,
                             const string &str2) {
    auto m = str1.length();
    auto n = str2.length();

    vector<int> prevRow(n + 1, 0);
    vector<int> currRow(n + 1, 0);

    for (int j = 0; j <= n; j++) {
        prevRow[j] = j;
    }

    for (int i = 1; i <= m; i++) {
        currRow[0] = i;

        for (int j = 1; j <= n; j++) {
            if (str1[i - 1] == str2[j - 1]) {
                currRow[j] = prevRow[j - 1];
            } else {
                currRow[j] = 1
                             + min(

                        // Insert
                        currRow[j - 1],
                        min(

                                // Remove
                                prevRow[j],

                                // Replace
                                prevRow[j - 1]));
            }
        }

        prevRow = currRow;
    }

    return currRow[n];
}

int searchMinLevenshteinDistanceInSentence(const string &sentence, const string &target) {
    istringstream stream(sentence);
    string currentWord;
    int minDistance = 100;

    while (stream >> currentWord) {

        // Remove punctuation
        currentWord.erase(remove_if(currentWord.begin(), currentWord.end(), ::ispunct),
                          currentWord.end());

        currentWord = trim(currentWord);

        if (currentWord.empty()) {
            continue;
        }

        // Compute Levenshtein distance
        int distance = levenshteinTwoMatrixRows(currentWord, target);
        if (distance < minDistance) {
            minDistance = distance;
        }
    }
    return minDistance;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_github_lucascalheiros_fuzzysearch_FuzzySearch_levenshtein(
        JNIEnv *env,
        jobject /* this */,
        jstring jtext,
        jstring jquery) {

    string text = jstringToString(env, jtext);
    string query = jstringToString(env, jquery);
    int distance = searchMinLevenshteinDistanceInSentence(text, query);
    return static_cast<jint>(distance); // Return as jint (Java int)
}



