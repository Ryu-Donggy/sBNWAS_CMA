package com.familidata.sbnwas_cma.room

object PropertyObj {
    const val LOGIN_ID = "LOGIN_ID"  //이멜형식
    const val USER_NAME = "USER_NAME" //사람이름
    const val USER_ID = "USER_ID"
    const val AGE = "AGE";
    const val GENDER = "GENDER";
    const val HEIGHT = "HEIGHT";
    const val WEIGHT = "WEIGHT";

    const val ENCRYPTION_CODE = "ENCRYPTION_CODE"
    const val SCHEDULER_CNT = "SCHEDULER_CNT"
    const val WATCH_ID = "WATCH_ID"


    const val RETRY_SEND_ERR = "RETRY_SEND_ERR"
    const val RETRY_MIN_BLP = "RETRY_MIN_BLP"
    const val SYNC_CMA_SET = "SYNC_CMA_SET"
    const val SYNC_DEVICE_SET = "SYNC_DEVICE_SET"
    const val SYNC_WORK_SET = "SYNC_WORK_SET"

    const val SYNC_BIO_SET = "SYNC_BIO_SET"
    const val SYNC_SLEEP_SET = "SYNC_SLEEP_SET"


    //    생체정보 데이터 전송 에러시 재전송 시도	 RETRY_SEND_ERR	B	TRUE		"true : FDSS로 생체 정보 전송 시, 전송 오류 데이터 포함하여 재 전송
//                                                                               false : FDSS로 생체 정보 전송 시, 해당 데이터만 전송"
//    워치 측정 혈압 데이터 수집 주기	         RETRY_MIN_BLP	I	15	M	Health Connector의 혈압 데이터를 가져오는 주기(분 간격)
//    CMA 시스템 설정 동기화 주기	             SYNC_CMA_SET	I	1	D	CMS로 부터 CMA 시스템 설정을 가져오는 주기(일 간격)
//    디바이스 설정 동기화 주기	             SYNC_DEVICE_SET	I	1	D	CMS로 부터 사용자별 생체 정보 수집 시스템 설정을 가져오는 주기(일 간격)
//    근무계획 동기화 주기	                 SYNC_WORK_SET	I	1	D	"CMS로 부터 사용자별 근무 계획을 가져오는 주기(일 간격)
//                                                                    - 근무 계획은 1주일 데이터를 가져옴
//                                                                    - 가져온 근무 계획으로 데이터를 갱신한다. (근무가 시작되거나 종료된 데이터는 제외)"
//    CMS 와 BIO SYNC 최초 수행 여부       SYNC_BIO_SET (최초 동기화 후 "Y"로 설정)
//    HEALTH CONNECT SLEEP DATE 조회 여부 SYNC_SLEEP_SET (최초 동기화 후 "Y"로 설정)
    
    const val TRUE = "TRUE"
    const val FALSE = "FALSE"


    object PreWork {
        const val WB_SW_WEAR = "WB_SW_WEAR"
        const val WB_BATTERY_FULL = "WB_BATTERY_FULL"
        const val WB_BIO_ECG = "WB_BIO_ECG"
        const val WB_BIO_HTR = "WB_BIO_HTR"
        const val WB_BIO_OXS = "WB_BIO_OXS"
        const val WB_BIO_BLP = "WB_BIO_BLP"
        const val WB_BIO_BAS = "WB_BIO_BAS"
        const val WB_RETRY_MIN_ALARM = "WB_RETRY_MIN_ALARM"

//        근무전 워치 착용여부 체크	    WB_SW_WEAR	B	TRUE		true : 근무 시작 1시간 전 부터, Watch 미 착용 여부를 조회하여 사용자에게 알림 (15분 간격) => 최소 간격
//        근무전 베터리잔량(70%) 체크	    WB_BATTERY_FULL	B	TRUE		true : 근무 시작 1시간 전 부터, Watch Battery 상태(70% 이하)를 조회하여 사용자에게 알림 (15분 간격) => 최소 간격
//        근무전 심전도 측정	        WB_BIO_ECG	B	TRUE		true : 근무 시작 1시간 전 부터, 심전도 측정(1회) 여부 를 조회하여 사용자에게 알림 => WB_RETRY_MIN_ALARM
//        근무전 심박수 측정	        WB_BIO_HTR	B	FALSE		true : 근무 시작 1시간 전 부터, 심박수 측정(1회) 여부 를 조회하여 사용자에게 알림 => WB_RETRY_MIN_ALARM
//        근무전 산소포화도 측정	        WB_BIO_OXS	B	FALSE		true : 근무 시작 1시간 전 부터, 산소포화도 측정(1회) 여부 를 조회하여 사용자에게 알림 => WB_RETRY_MIN_ALARM
//        근무전 혈압 측정	            WB_BIO_BLP	B	FALSE		true : 근무 시작 1시간 전 부터, 혈압 측정(1회) 여부 를 조회하여 사용자에게 알림 => WB_RETRY_MIN_ALARM
//        근무전 심/폐음 측정	        WB_BIO_BAS	B	FALSE		true : 근무 시작 1시간 전 부터, 심음 또는 폐음 측정(1회) 여부 를 조회하여 사용자에게 알림 => WB_RETRY_MIN_ALARM
//        근무전 건강정보 미 측정 알림 주기	WB_RETRY_MIN_ALARM	I	30	M	근무 시작 1시간 전 부터,  생체 정보 측정 여부를 조회하여 사용자에게 알림을 주는 주기(분 간격)
    }


    object InWork {
        const val WI_SW_WEAR = "WI_SW_WEAR"
        const val WI_BIO_ECG = "WI_BIO_ECG"
        const val WI_BIO_HTR = "WI_BIO_HTR"
        const val WI_BIO_OXS = "WI_BIO_OXS"
        const val WI_BIO_BLP = "WI_BIO_BLP"
        const val WI_RETRY_MIN_ALARM = "WI_RETRY_MIN_ALARM"
        const val WI_GET_BIO_HOUR = "WI_GET_BIO_HOUR"
        const val WI_RETRY_MIN_WAP = "WI_RETRY_MIN_WAP"
        const val WI_BIO_GYR = "WI_BIO_GYR"
        const val WI_BIO_ACC = "WI_BIO_ACC"
        const val WI_BIO_GPS = "WI_BIO_GPS"
        const val WI_BIO_WAP = "WI_BIO_WAP"
        const val WI_BIO_BAS = "WI_BIO_BAS"

//        근무중 워치 착용여부 체크	    WI_SW_WEAR	B	TRUE		true : 근무 중, Watch 미 착용 여부를 조회하여 사용자에게 알림 (15분 간격) => 최소 간격
//        근무중 심전도 측정	        WI_BIO_ECG	B	FALSE		true : 근무 중, 심전도 측정(횟수는 WI_GET_BIO_HOUR 참조) 여부 를 조회하여 사용자에게 알림 => WI_RETRY_MIN_ALARM
//        근무중 심박수 측정	        WI_BIO_HTR	B	FALSE		true : 근무 중, 심박수 측정(횟수는 WI_GET_BIO_HOUR 참조) 여부 를 조회하여 사용자에게 알림 => WI_RETRY_MIN_ALARM
//        근무중 산소포화도 측정	        WI_BIO_OXS	B	FALSE		true : 근무 중, 산소포화도 측정(횟수는 WI_GET_BIO_HOUR 참조) 여부 를 조회하여 사용자에게 알림 => WI_RETRY_MIN_ALARM
//        근무중 혈압 측정	            WI_BIO_BLP	B	FALSE		true : 근무 중, 혈압 측정(횟수는 WI_GET_BIO_HOUR 참조) 여부 를 조회하여 사용자에게 알림 => WI_RETRY_MIN_ALARM
//        근무중 건강정보 미 측정 알림 주기	WI_RETRY_MIN_ALARM	I	30	M	근무 중,  생체 정보 측정 여부를 조회하여 사용자에게 알림을 주는 주기(분 간격)
//        근무중 건강정보 측정 주기	    WI_GET_BIO_HOUR	I	3	H	근무 중, 생체 정보 측정 주기(시간 간격)
//        근무중 근무위치 확인 주기	    WI_RETRY_MIN_WAP	I	30	M	근무 중, 근무 위치 이탈 여부 를 조회하여 사용자에게 알림(분 간격)
//        근무중 자이로센서값 수집	    WI_BIO_GYR	B	TRUE		true : 근무 중, 자이로 센서 값 수집
//        근무중 가속도계센서값 수집	    WI_BIO_ACC	B	TRUE		true : 근무 중, 가속도 센서 값 수집
//        근무중 GPS값 수집	        WI_BIO_GPS	B	TRUE		true : 근무 중, GPS 센서 값 수집
//        근무중 근무위치 체크	        WI_BIO_WAP	B	TRUE		true : 근무 중, 근무 위치 이탈 여부 조회
    }
}
