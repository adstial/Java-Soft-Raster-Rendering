package app.top;

public enum Schedule {
    PreStartup {
        @Override
        public Schedule next() {
            return Startup;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 0;
        }
    },

    Startup {
        @Override
        public Schedule next() {
            return PostStartup;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 1;
        }
    },

    PostStartup {
        @Override
        public Schedule next() {
            return First;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 2;
        }
    },

    // loop

    First {
        @Override
        public Schedule next() {
            return PreUpdate;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 3;
        }
    },

    PreUpdate {
        @Override
        public Schedule next() {
            return StateTransition;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 4;
        }
    },

    StateTransition {
        @Override
        public Schedule next() {
            return RunFixedMainLoop;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 5;
        }
    },

    RunFixedMainLoop {
        @Override
        public Schedule next() {
            return Update;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 6;
        }
    },

    Update {
        @Override
        public Schedule next() {
            return PostUpdate;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 7;
        }
    },

    PostUpdate {
        @Override
        public Schedule next() {
            return PreRender;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 8;
        }
    },

    PreRender {
        @Override
        public Schedule next() {
            return Render;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 9;
        }
    },

    Render {
        @Override
        public Schedule next() {
            return PostRender;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 10;
        }
    },

    PostRender {
        @Override
        public Schedule next() {
            return Frame;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 11;
        }
    },

    Frame {
        @Override
        public Schedule next() {
            return First;
        }

        @Override
        public boolean willEnd() {
            return true;
        }

        @Override
        public int getIndex() {
            return 12;
        }
    };

    public abstract Schedule next();
    public abstract boolean willEnd();
    public abstract int getIndex();

    public static Schedule New() {
        return PreStartup;
    }

    public static int ScheduleSize() {
        return values().length;
    }


}
