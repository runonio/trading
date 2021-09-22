/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.view.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.Collections;
/**
 * @author ccsweets
 */
public class BrowserUtil {
    /**
     * 크롬드라이버 정보
     */
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "resources/chromedriver.exe";

    /**
     * 크롬에 차트를 로드한다
     * @param filePath filePath
     */
    public static void loadChromeByFile(String filePath){
        loadChromeByFile(WEB_DRIVER_PATH,filePath);
    }

    /**
     * 크롬에 차트를 로드한다
     * @param chromeDriverPath 크롬드라이버 위치
     * @param filePath filePath
     */
    public static void loadChromeByFile(String chromeDriverPath,String filePath){

        File chromeFileCheck = new File(WEB_DRIVER_PATH);
        if(!chromeFileCheck.exists()){
            JarUtil.copyFromJarFile("chromedriver.exe","resources");
        }

        System.setProperty(WEB_DRIVER_ID, chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(options);
        driver.get("file://"+filePath);
    }
}
