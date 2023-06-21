package me.sepehrasadiyan.model.enums;

public enum BillState {
    CREATE(0),
    WAITING(1),
    SUCCESS(2),
    ERROR(3),
    ERROR_REVERSE_MONEY(4);

    BillState(int create) {

    }


    public int getBillState() {
        switch (this) {
            case CREATE:
                return 0;
            case WAITING:
                return 1;
            case SUCCESS:
                return 2;
            case ERROR:
                return 3;
            case ERROR_REVERSE_MONEY:
                return 4;
            default:
                return 5;
        }
    }

    public String getBillStateString() {
        switch (this) {
            case CREATE:
                return "CREATE";
            case WAITING:
                return "WAITING";
            case SUCCESS:
                return "SUCCESS";
            case ERROR:
                return "ERROR";
            case ERROR_REVERSE_MONEY:
                return "ERROR_REVERSE_MONEY";
            default:
                return "ERROR_IN_SERVER";
        }
    }

}
