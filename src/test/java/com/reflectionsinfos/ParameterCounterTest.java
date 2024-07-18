package com.reflectionsinfos;

import org.junit.jupiter.api.Test;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class ParameterCounterTest {


    @Test
    void test() {
        PythonCheckVerifier.verify("src/test/resources/checks/tooManyParametersCustom.py", new ParameterCounter());
    }

//    @Test
//    void custom() {
//        ParameterCounter check = new ParameterCounter();
//        check.max = 3;
//        PythonCheckVerifier.verify("src/test/resources/checks/tooManyParametersCustom.py", check);
//    }

}
