package com.demo.ming.webview.login;

import com.google.gson.annotations.SerializedName;
import com.tamic.novate.Novate;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/21.
 */

public class LoginResult  {


    /**
     * body : {"ECWEB-JWTSSO-TOKEN":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwZXJzb25BY2NvdW50Ijoib3VqaWFudGluZyIsInBlcnNvblN0YXR1cyI6bnVsbCwic3lzQ29kZSI6bnVsbCwib3JnTmFtZSI6Iuays-a6kOW4guS6uuWKm-i1hOa6kOWSjOekvuS8muS_nemanOWxgFxc5bGA5Yqe5YWs5a6kIiwicGVyc29uU2V0dXBJZCI6bnVsbCwib3BlbmZpcmVYbXBwRG9tYWluIjpudWxsLCJwaG90b05hbWUiOm51bGwsIm9wZW5maXJlU2VydmVyIjpudWxsLCJzdGFmZkxpc3QiOlt7ImlkIjoiNDAyODlmODE2NjViZTMwNzAxNjY1YzFjMGRkOTAwMjMiLCJvcmdhbml6YXRpb24iOnsiaWQiOiI0MDI4OWY4MTY2NWJlMzA3MDE2NjVjMWMwOWIzMDAxNyIsImNvZGUiOm51bGwsIm5hbWUiOiLlsYDlip7lhazlrqQiLCJwYXJlbnRJZCI6IjQwMjg5ZjgxNjY1YmUzMDcwMTY2NWMxYzAzNmYwMDA0IiwiaXNIYXNDaGlsZCI6MSwiZGVzY3JpcHRpb24iOm51bGwsImxldmVsIjoyLCJvcmRlck5vIjoiMTAyMCIsInN0YXR1cyI6MSwiaXNSZWFsT3JnIjpudWxsLCJ0eXBlIjpudWxsLCJtYW5hZ2VPcmdJZCI6bnVsbCwic2hvcnROYW1lIjpudWxsLCJ0cmVlSWQiOm51bGwsInRyZWVPcmRlck5vIjpudWxsLCJpc01hbmFnZU9yZyI6MCwianNvbkRhdGEiOm51bGwsImdlbmVyYXRlVHlwZSI6bnVsbH0sIm9yZ09yZGVyTm8iOiIwMTExXzEwMjAiLCJvcmdGdWxsTmFtZSI6Iuays-a6kOW4guS6uuWKm-i1hOa6kOWSjOekvuS8muS_nemanOWxgFxc5bGA5Yqe5YWs5a6kIiwicm9vdE9yZ0NvZGUiOiJIWVJTSk9BIiwicGVyc29uIjpudWxsLCJvcmRlck5vIjoiMDA5NSIsInN0YXR1cyI6MSwicG9zaXRpb25UeXBlIjowLCJwb3NpdGlvbk5hbWUiOiIiLCJzdGFmZlBvc2l0aW9ucyI6W119XSwicGVyc29uQ29kZSI6bnVsbCwicGVyc29uTmFtZSI6Iuasp-WJkeWptyIsInNlcmlhbFZlcnNpb25VSUQiOi04OTI5MjMyMzY5MTUzMDU3Njg4LCJ0b2RvSGludCI6bnVsbCwicGFzc3dvcmQiOm51bGwsInBlcnNvbk1vYmlsZSI6IjE4ODA3NTExMTQ4IiwidWlTdHlsZSI6bnVsbCwibGVmdE1lbnVNaW4iOm51bGwsIm9yZ0NvZGUiOiJIWVJTSk9BIiwic3lzTmFtZSI6bnVsbCwicGVyc29uV29ya1RlbCI6bnVsbCwicGVyc29uSWQiOiI0MDI4OWY4MTY2NWJlMzA3MDE2NjVjMWMwZDc2MDAyMiIsInBlcnNvbk1haWwiOiIiLCJoNWluZGV4VXJsIjpudWxsfQ.YF23cg5WZqS6zOuMmmwR9w2nOXmGJDTUPiFnuMBwULY","JSESSIONID":"7F2911617A35BD40693EF69B7DC42141"}
     * code : success
     * message : 处理成功！
     */

    private BodyBean body;
    private String code;
    private String message;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class BodyBean implements Serializable{
        /**
         * ECWEB-JWTSSO-TOKEN : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwZXJzb25BY2NvdW50Ijoib3VqaWFudGluZyIsInBlcnNvblN0YXR1cyI6bnVsbCwic3lzQ29kZSI6bnVsbCwib3JnTmFtZSI6Iuays-a6kOW4guS6uuWKm-i1hOa6kOWSjOekvuS8muS_nemanOWxgFxc5bGA5Yqe5YWs5a6kIiwicGVyc29uU2V0dXBJZCI6bnVsbCwib3BlbmZpcmVYbXBwRG9tYWluIjpudWxsLCJwaG90b05hbWUiOm51bGwsIm9wZW5maXJlU2VydmVyIjpudWxsLCJzdGFmZkxpc3QiOlt7ImlkIjoiNDAyODlmODE2NjViZTMwNzAxNjY1YzFjMGRkOTAwMjMiLCJvcmdhbml6YXRpb24iOnsiaWQiOiI0MDI4OWY4MTY2NWJlMzA3MDE2NjVjMWMwOWIzMDAxNyIsImNvZGUiOm51bGwsIm5hbWUiOiLlsYDlip7lhazlrqQiLCJwYXJlbnRJZCI6IjQwMjg5ZjgxNjY1YmUzMDcwMTY2NWMxYzAzNmYwMDA0IiwiaXNIYXNDaGlsZCI6MSwiZGVzY3JpcHRpb24iOm51bGwsImxldmVsIjoyLCJvcmRlck5vIjoiMTAyMCIsInN0YXR1cyI6MSwiaXNSZWFsT3JnIjpudWxsLCJ0eXBlIjpudWxsLCJtYW5hZ2VPcmdJZCI6bnVsbCwic2hvcnROYW1lIjpudWxsLCJ0cmVlSWQiOm51bGwsInRyZWVPcmRlck5vIjpudWxsLCJpc01hbmFnZU9yZyI6MCwianNvbkRhdGEiOm51bGwsImdlbmVyYXRlVHlwZSI6bnVsbH0sIm9yZ09yZGVyTm8iOiIwMTExXzEwMjAiLCJvcmdGdWxsTmFtZSI6Iuays-a6kOW4guS6uuWKm-i1hOa6kOWSjOekvuS8muS_nemanOWxgFxc5bGA5Yqe5YWs5a6kIiwicm9vdE9yZ0NvZGUiOiJIWVJTSk9BIiwicGVyc29uIjpudWxsLCJvcmRlck5vIjoiMDA5NSIsInN0YXR1cyI6MSwicG9zaXRpb25UeXBlIjowLCJwb3NpdGlvbk5hbWUiOiIiLCJzdGFmZlBvc2l0aW9ucyI6W119XSwicGVyc29uQ29kZSI6bnVsbCwicGVyc29uTmFtZSI6Iuasp-WJkeWptyIsInNlcmlhbFZlcnNpb25VSUQiOi04OTI5MjMyMzY5MTUzMDU3Njg4LCJ0b2RvSGludCI6bnVsbCwicGFzc3dvcmQiOm51bGwsInBlcnNvbk1vYmlsZSI6IjE4ODA3NTExMTQ4IiwidWlTdHlsZSI6bnVsbCwibGVmdE1lbnVNaW4iOm51bGwsIm9yZ0NvZGUiOiJIWVJTSk9BIiwic3lzTmFtZSI6bnVsbCwicGVyc29uV29ya1RlbCI6bnVsbCwicGVyc29uSWQiOiI0MDI4OWY4MTY2NWJlMzA3MDE2NjVjMWMwZDc2MDAyMiIsInBlcnNvbk1haWwiOiIiLCJoNWluZGV4VXJsIjpudWxsfQ.YF23cg5WZqS6zOuMmmwR9w2nOXmGJDTUPiFnuMBwULY
         * JSESSIONID : 7F2911617A35BD40693EF69B7DC42141
         */

        @SerializedName("ECWEB-JWTSSO-TOKEN")
        private String ECWEBJWTSSOTOKEN;
        private String JSESSIONID;

        public String getECWEBJWTSSOTOKEN() {
            return ECWEBJWTSSOTOKEN;
        }

        public void setECWEBJWTSSOTOKEN(String ECWEBJWTSSOTOKEN) {
            this.ECWEBJWTSSOTOKEN = ECWEBJWTSSOTOKEN;
        }

        public String getJSESSIONID() {
            return JSESSIONID;
        }

        public void setJSESSIONID(String JSESSIONID) {
            this.JSESSIONID = JSESSIONID;
        }
    }
}
