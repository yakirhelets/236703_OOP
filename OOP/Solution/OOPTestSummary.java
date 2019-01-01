package OOP.Solution;

import OOP.Provided.OOPResult;

import java.util.Map;

public class OOPTestSummary {
    private int numSuccesses = 0;
    private int numFailures = 0;
    private int numExceptionMismathces = 0;
    private int numErrors = 0;

    public OOPTestSummary(Map<String, OOPResult> testMap) {
        testMap.forEach(
                (key, value) -> {
                    switch (value.getResultType()) {
                        case SUCCESS:
                            numSuccesses++;
                            break;
                        case FAILURE:
                            numFailures++;
                            break;
                        case EXPECTED_EXCEPTION_MISMATCH:
                            numExceptionMismathces++;
                            break;
                        case ERROR:
                            numErrors++;
                            break;
                    }
                }
        );
    }

    public int getNumSuccesses() {
        return numSuccesses;
    }

    public int getNumFailures() {
        return numFailures;
    }

    public int getNumExceptionMismathces() {
        return numExceptionMismathces;
    }

    public int getNumErrors() {
        return numErrors;
    }
}
