package com.thoughtworks.i1.quartz.service;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.QrtzHistory;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.thoughtworks.i1.quartz.domain.JobDetailVO.fromJobDetail;
import static com.thoughtworks.i1.quartz.domain.TriggerVO.fromTrigger;

public class QrtzHistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrtzHistoryService.class);

    public static final String INSERT_QRTZ_HISTORY = "INSERT INTO QRTZ_HISTORY(" +
            "SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,JOB_NAME,JOB_GROUP,START_TIME,END_TIME,IS_NORMAL,EXCEPTION_DESC) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SELECT_QRTZ_HISTORY = "SELECT SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,JOB_NAME,JOB_GROUP,START_TIME,END_TIME,IS_NORMAL,EXCEPTION_DESC " +
            "FROM QRTZ_HISTORY WHERE TRIGGER_NAME = ? AND TRIGGER_GROUP = ?";

    public QrtzHistoryService() {
    }

    public List<QrtzHistory> getQrtzHistorysByTrigger(String triggerName, String triggerGroup){
        List<QrtzHistory> qrtzHistories = Lists.newArrayList();
        Connection conn = null;
        try {
            conn = DBConnectionManager.getInstance().getConnection("herendh");
            qrtzHistories = getQrtzHistorysByTrigger(conn, triggerName, triggerGroup);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        } finally {
            closeConnection(conn);
            return  qrtzHistories;
        }

    }

    public List<QrtzHistory> getQrtzHistorysByTrigger(Connection conn, String triggerName, String triggerGroup)
            throws IOException, SQLException {
        List<QrtzHistory> qrtzHistories = Lists.newArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(SELECT_QRTZ_HISTORY);
            ps.setString(1, triggerName);
            ps.setString(2, triggerGroup);

            rs = ps.executeQuery();
            while(rs.next()){
                QrtzHistory qrtzHistory = new QrtzHistory();
                qrtzHistory.setSchedName(rs.getString(1));
                qrtzHistory.setTriggerName(rs.getString(2));
                qrtzHistory.setTriggerGroup(rs.getString(3));
                qrtzHistory.setJobName(rs.getString(4));
                qrtzHistory.setJobGroup(rs.getString(5));
                qrtzHistory.setStartTime(rs.getLong(6));
                qrtzHistory.setEndTime(rs.getLong(7));
                qrtzHistory.setIsNormal(rs.getInt(8));
                qrtzHistory.setExceptionDesc(Strings.emptyToNull(rs.getString(9)));
                qrtzHistories.add(qrtzHistory);
            }

            ps.close();

        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            return qrtzHistories;
        }
    }



    public void insertQrtzHistory(QrtzHistory qrtzHistory){
        Connection conn = null;
        try {
            conn = DBConnectionManager.getInstance().getConnection("herendh");

            insertQrtzHistory(conn, qrtzHistory);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        } finally {
            closeConnection(conn);
        }
    }

    public void insertQrtzHistory(Connection conn, QrtzHistory qrtzHistory)
            throws IOException, SQLException {
        conn.setAutoCommit(false);
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(INSERT_QRTZ_HISTORY);
            ps.setString(1, qrtzHistory.getSchedName());
            ps.setString(2, qrtzHistory.getTriggerName());
            ps.setString(3, qrtzHistory.getTriggerGroup());
            ps.setString(4, qrtzHistory.getJobName());
            ps.setString(5, qrtzHistory.getJobGroup());
            ps.setLong(6, qrtzHistory.getStartTime());
            ps.setLong(7, qrtzHistory.getEndTime());
            ps.setInt(8, qrtzHistory.getIsNormal());
            ps.setString(9, qrtzHistory.getExceptionDesc());

            ps.executeUpdate();
            ps.close();
            conn.commit();
        } catch (Exception e){
            conn.rollback();
        } finally {
            closeStatement(ps);
        }
    }

    protected static void closeResultSet(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException ignore) {
            }
        }
    }

    protected static void closeStatement(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }

    protected static void closeConnection(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }


}