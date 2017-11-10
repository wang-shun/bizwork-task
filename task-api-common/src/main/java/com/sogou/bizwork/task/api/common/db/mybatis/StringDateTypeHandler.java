package com.sogou.bizwork.task.api.common.db.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 用于jdbc类型TimeStamp与Java类型String的相互转换
 * @author yangbing
 * @date 2016-7-21
 * @version 1.0.0
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class StringDateTypeHandler extends BaseTypeHandler<String> {

	private static final Logger logger = LoggerFactory.getLogger(StringDateTypeHandler.class);
	private static ThreadLocal<DateFormat> localDF = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * for database initial date value '0000-00-00 00:00:00'
	 */
	private static final String specialDateString = "0000-00-00 00:00:00";

	@Override
	public String getNullableResult(ResultSet resultset, String s) throws SQLException {
		Timestamp tStamp = resultset.getTimestamp(s);
		return getDateStringFromTimeStamp(tStamp);
	}

	@Override
	public String getNullableResult(ResultSet resultset, int i) throws SQLException {
		Timestamp tStamp = resultset.getTimestamp(i);
		return getDateStringFromTimeStamp(tStamp);
	}

	@Override
	public String getNullableResult(CallableStatement callablestatement, int i) throws SQLException {
		// TODO Auto-generated method stub
		Timestamp tStamp = callablestatement.getTimestamp(i);
		return getDateStringFromTimeStamp(tStamp);
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, String arg2, JdbcType arg3) throws SQLException {
		// TODO Auto-generated method stub
		if (arg3 == JdbcType.TIMESTAMP) {
			if (specialDateString.equals(arg2)) {
				logger.warn("zero timeString '{}', ignore it.", arg2);
				return;
			}
			Date date = null;
			try {
				date = localDF.get().parse(arg2);
				localDF.remove();
			} catch (ParseException e) {
				logger.error("sdf parse error, illegal timeString '{}', ignore it.", arg2, e);
			}
			if (date != null) {
				arg0.setTimestamp(arg1, new Timestamp(date.getTime()));
			}
		}
	}

	private String getDateStringFromTimeStamp(Timestamp tStamp) {
		if (tStamp == null) {
			if(logger.isDebugEnabled()){
				logger.debug("return full zero dateString '{}'", specialDateString);
			}
			return specialDateString;
		}
		String dateString = localDF.get().format(new Date(tStamp.getTime()));
		localDF.remove();
		
		return dateString;
	}

}
