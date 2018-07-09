package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/7/9 21:51
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class OssTokenVO extends BaseModel {
    /**
     * StatusCode : 200
     * AccessKeyId : STS.NJ9aGzhM2nsGrCL6EKTfqcpTr
     * AccessKeySecret : EyfGq92CEHrBuKtgbSUNd9t6AN9SyVueS62saN7m6FX2
     * SecurityToken : CAISwAN1q6Ft5B2yfSjIr4mMKv3OhZIT2bGscGX90kUeWOldjLX/kDz2IHBPf3FtBOkav/Q0mmFU6P8SlrJ+W4NIX0rNaY4vtssKrlvwPtuZ6pTksuBb0sX92vwRmbG/jqHoeOzcYI735p7PAgm2EEYRrJL+cTK9JbbMU/mggoJmadI6RxSxaSE8av5dOgplrr0YVx7WLu3/HRP2pWDSAUF02G97kngt0bmj5cee5xHC7iX90fRHi4n1L4SpSMNuMZxjMbKyx/ckUqvazBZZ8QRRltdxl7cW0D3ApM24Hl1N4g2PKfbp6tloJQMbAa8hAPxgrePgsvd6t+fPjP7/qXB3MPpSTj7USa253cLAA5nJDNsicqvhMHm//rLGP5Lu4QQ/eiBZZkEYe8s5LGJrTBU0UnTAJ7Sg5UrBb0DhKd+M27pk1oFunRe6v4iKNkCJXq7c2D4Af4UxdEQ1LRUb/xS4LPNeK10WKww5W+nFFtwvVn0E9/O05z+1fzZ703Ras8f5Y/7roa0FYe39JMkWitFFOckf7zp6FAqmF+z32l1nfWhkUKtQ17L2JZi85bmKzeOee+faDeodvVFXYbInP84e1e86GoABe3PpEafirlcqXtdnsq3wX4RkVohByTomLFBQ6FmEHccBWEhjWSefmQhDugTA6vawA6m5hzHSbzWPHANNiWhURoYPMsAwubf4CgsGOEpRlxkAc/kyQ47bLCK7ASLUPVwpuZNx99+R/+P0LhxXpl8m+NefAN0eMWL2fI7SmjES/eA=
     * Expiration : 2018-07-09T14:03:30Z
     */

    private String StatusCode;
    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private String Expiration;

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }
}
