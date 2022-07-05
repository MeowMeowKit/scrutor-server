/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import dtos.Option;
import dtos.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utils.DBUtils;


public class TagDAO {

    public static ArrayList<Tag> getTags() {
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;

        ArrayList<Tag> list = new ArrayList<>();

        try {
            conn = DBUtils.makeConnection();

            if (conn != null) {
                String sql = "SELECT tagId, name FROM tbl_Tag";
                preStm = conn.prepareStatement(sql);
                rs = preStm.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        String tagId = rs.getString("tagId");
                        String name = rs.getString("name");
                        list.add(new Tag(tagId, name));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
