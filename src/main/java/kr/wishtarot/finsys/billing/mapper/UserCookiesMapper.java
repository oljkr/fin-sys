package kr.wishtarot.finsys.billing.mapper;

import kr.wishtarot.finsys.billing.model.UserCookie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCookiesMapper {
    UserCookie selectUserCookieByUserId(@Param("userId") String userId);
    void insertUserCookie(UserCookie userCookie);
    void updateUserCookie(UserCookie userCookie);
}
