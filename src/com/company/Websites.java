package com.company;

import java.util.List;

public interface Websites {
    abstract void submit(String url);
    abstract List<List<String>> load_testCases(String url) throws Exception;
}
