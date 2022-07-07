package dtos;

public class AttemptOption {
    private Option option;
    private boolean isChecked;

    public AttemptOption() {
        this.option = null;
        this.isChecked = false;
    }

    public AttemptOption(Option option, boolean isChecked) {
        this.option = option;
        this.isChecked = isChecked;
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
