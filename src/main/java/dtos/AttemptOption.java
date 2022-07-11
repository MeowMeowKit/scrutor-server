package dtos;

public class AttemptOption {
    private String attemptOptionId;
    private Option option;
    private boolean isChecked;

    public AttemptOption() {
        this.option = null;
        this.isChecked = false;
    }

    public AttemptOption(String attemptOptionId, Option option, boolean isChecked) {
        this.attemptOptionId = attemptOptionId;
        this.option = option;
        this.isChecked = isChecked;
    }

    public String getAttemptOptionId() {
        return attemptOptionId;
    }

    public void setAttemptOptionId(String attemptOptionId) {
        this.attemptOptionId = attemptOptionId;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        isChecked = isChecked;
    }
}
