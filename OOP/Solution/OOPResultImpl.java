package OOP.Solution;

import OOP.Provided.OOPResult;

public class OOPResultImpl implements OOPResult {

    private OOPTestResult resultType;
    private String resultMessage;

    public OOPResultImpl(OOPTestResult resultType, String resultMessage) {
        this.resultType = resultType;
        this.resultMessage = resultMessage;
    }

    /**
     * @return the result type, which is one of four possible type. See OOPTestResult.
     */
    @Override
    public OOPTestResult getResultType() {
        return resultType;
    }

    /**
     * @return the message of the result in case of an error.
     */
    @Override
    public String getMessage() {
        return resultMessage;
    }

    /**
     * Equals contract between two test results.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != OOPResultImpl.class) {
            return false;
        }
        if (this.getMessage().equals(((OOPResultImpl)obj).getMessage())
                && this.getResultType() == ((OOPResultImpl)obj).getResultType()) {
            return true;
        } else {
            return false;
        }
    }
}
