package me.sepehrasadiyan.model.registry;

public enum RegistryType {
    CREATE(0),
    ADD(1),
    WITHDRAW(2);

    RegistryType(int create) {

    }


    public int getRegistryType() {
        switch(this) {
            case CREATE:
                return 0;
            case ADD:
                return 1;
            case WITHDRAW:
                return 2;
        }
        return 5;
    }
}
