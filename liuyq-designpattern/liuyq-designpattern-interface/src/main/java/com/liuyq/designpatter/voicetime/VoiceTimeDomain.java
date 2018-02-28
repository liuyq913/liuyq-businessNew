package com.liuyq.designpatter.voicetime;

/**
 * Created by liuyq on 2017/12/18.
 */
public interface VoiceTimeDomain {
    /**
     * 支持mp3 及 m4a格式
     * @param url
     * @return
     */
    public Long getVoiceTimeByUrl(String url);
}
