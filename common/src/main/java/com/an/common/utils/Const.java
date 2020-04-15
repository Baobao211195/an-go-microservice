package com.an.common.utils;

import java.util.Objects;

public class Const {
    public static class USER {
        public static String FAKE_PASSWORD = "FAKE_PASSWORD";
        public static int DEFAULT_PASS_LENGTH = 6;
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
        public static String STATUS_UNAPPROVED = "2";

        public static String TYPE_CUSTOMER = "0";
        public static String TYPE_DRIVER = "1";

        public static String USERNAME_CONNECT_CHAR = "@";

        public static String GOOGLE_USER_PREFIX = "GG:";
    }

    public static class USER_PROFILE {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
    }

    public static class ROLE {

    }

    public static class USER_LOCATION {
        public static Long TIME_TO_LIVE = 60L;
        public static Long DEFAULT_DISTANCE = 1L;
    }

    public static class SHARE_LOCATION {
        public static Double DEFAULT_DISTANCE = 0.5;
    }

    public static class OTP {
        public static Long TIME_TO_LIVE = 600L;
        public static int LENGTH = 4;
        public static String OK = "00";
    }

    public static class DRIVER_CANCEL {
        public static Long TIME_TO_LIVE = 3600L;
    }

    public static class DRIVER_ON_OFF {
        public static String ON = "1";
        public static String OFF = "0";
    }

    public static class USER_WALLET {
        public static String OPERATE_MINUS = "0";
        public static String OPERATE_ADDED = "1";
        public static String BALANCE_TYPE_DEPOSIT = "0";
        public static String BALANCE_TYPE_PROMOTION = "1";
        public static String BALANCE_TYPE_DISTANCE = "2";

        public static Double INVITE_BONUS = 50000D;
    }

    public static class PRODUCT {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
        public static String TYPE_DAY = "1";
        public static String TYPE_WEEK = "2";
        public static String TYPE_MONTH = "3";
    }

    public static class PROMOTION {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
        public static String TYPE_VALUE = "0";
        public static String TYPE_PERCENT = "1";
    }

    public static class USER_PROMOTION {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
    }

    public static class BOOKING_ISSUE {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
    }

    public static class USER_SERVICE {
        public static String STATUS_NEW = "0";
        public static String STATUS_APPROVED = "1";
        public static String STATUS_CANCEL = "2";
    }

    public static class SERVICE_GROUP {
        public static Long BIKE = 1L;
        public static Long CAR = 2L;
        public static Long CAR7 = 3L;
        public static Long TRUCK = 4L;
    }

    public static class SERVICE {
        public static Long GO_BIKE = 1L;
        public static Long GO_CAR = 2L;
        public static Long GO_CAR7 = 3L;
        public static Long VAN = 4L;
        public static Long TRIP = 5L;
        public static Long SHARE_BIKE = 6L;
        public static Long SHARE_CAR = 7L;
        public static Long SHARE_CAR7 = 8L;
        public static String STATUS_ON = "1";
        public static String STATUS_OFF = "0";

        public static String convertIdToName(Long id){
            if (Objects.equals(id, GO_BIKE)) {
                return "Go Bike";
            } else if (Objects.equals(id, GO_CAR)) {
                return "Go Car";
            } else if (Objects.equals(id, GO_CAR)) {
                return "Go Car (7 seats)";
            } else if (Objects.equals(id, VAN)) {
                return "Van";
            } else if (Objects.equals(id, TRIP)) {
                return "Trip";
            } else if (Objects.equals(id, SHARE_BIKE)) {
                return "Share Bike";
            } else if (Objects.equals(id, SHARE_CAR)) {
                return "Share Car";
            } else if (Objects.equals(id, SHARE_CAR7)) {
                return "Share Car (7 seats)";
            }
            return "";
        }
    }

    public static class BOOKING {
        public static String STATUS_QUEUE = "0";
        public static String STATUS_ONGOING = "1";
        public static String STATUS_FINISH = "2";
        public static String STATUS_CANCEL = "3";
        public static String STATUS_CANCEL_NO_DRIVER = "4";
        public static String STATUS_CANCEL_SYSTEM_ERROR = "5";

        public static String PAYMENT_STATUS_ON = "1";
        public static String PAYMENT_STATUS_OFF = "0";
    }

    public static class NOTIFICATION {
        public static String STATUS_ON = "1";
        public static String STATUS_OFF = "0";
    }

    public static class BOOKING_PATH {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
    }

    public static class DRIVER_SCHEDULE {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
        public static String STATUS_PROCESSED = "2";
    }

    public static class USER_INVITE {
        public static String STATUS_NEW = "0";
        public static String STATUS_PROCESSED = "1";
    }

    public static class SERVICE_FEE {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";
    }

    public static class APP_CONFIG {
        public static String STATUS_OFF = "0";
        public static String STATUS_ON = "1";

        public static String CONFIG_CODE_RUSH_HOUR = "RUSH_HOUR";
    }

    public static class WS {
        public static String OK = "1";
        public static String NOK = "0";
    }
}
