/*
 * Copyright 2023 by Web3Bench Project
 * This work was based on the OLxPBench Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *  http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 */

 
package com.olxpbenchmark.benchmarks.web3benchmark.procedures;

import com.olxpbenchmark.api.SQLStmt;
import com.olxpbenchmark.benchmarks.web3benchmark.WEB3Config;
import com.olxpbenchmark.benchmarks.web3benchmark.WEB3Constants;
import com.olxpbenchmark.benchmarks.web3benchmark.WEB3Util;
import com.olxpbenchmark.benchmarks.web3benchmark.WEB3Worker;
import com.olxpbenchmark.benchmarks.web3benchmark.procedures.WEB3Procedure;
import com.olxpbenchmark.distributions.ZipfianGenerator;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class R22 extends WEB3Procedure {

    private static final Logger LOG = Logger.getLogger(R22.class);

    // Small range or list of values on: hash or to_address or from_address in transaction table
    public SQLStmt query_hash_SQL = new SQLStmt(
            "select "
                + "* "
                + "from transactions "
                + "where "
                + "hash in (?, ?, ?, ?) "
                + "and to_address <> from_address "
    );
    private PreparedStatement query_hash_stmt = null;

    public ResultSet run(Connection conn, Random gen,  WEB3Worker w, int startNumber, int upperLimit, int numScale) throws SQLException {
        boolean trace = LOG.isTraceEnabled();

        // initializing all prepared statements
        query_hash_stmt = this.getPreparedStatement(conn, query_hash_SQL);

        String hash1 = WEB3Util
                .convertToTxnHashString(WEB3Util.randomNumber(1, WEB3Config.configTransactionsCount * numScale, gen));
        String hash2 = WEB3Util
                .convertToTxnHashString(WEB3Util.randomNumber(1, WEB3Config.configTransactionsCount * numScale, gen));
        String hash3 = WEB3Util
                .convertToTxnHashString(WEB3Util.randomNumber(1, WEB3Config.configTransactionsCount * numScale, gen));
        String hash4 = WEB3Util
                .convertToTxnHashString(WEB3Util.randomNumber(1, WEB3Config.configTransactionsCount * numScale, gen));

        query_hash_stmt.setString(1, hash1);
        query_hash_stmt.setString(2, hash2);
        query_hash_stmt.setString(3, hash3);
        query_hash_stmt.setString(4, hash4);
        if (trace) LOG.trace("query_stmt R22 START");
        ResultSet rs = query_hash_stmt.executeQuery();
        if (trace) LOG.trace("query_stmt R22 END");
        
        if (trace) {
            String rs_hash = null;
            if (!rs.next()) {
                String msg = String.format("Failed to get transactions [hash in %s, %s, %s, %s]", hash1, hash2, hash3,
                        hash4);
                if (trace)
                    LOG.warn(msg);
                // throw new RuntimeException(msg);
            } else {
                rs_hash = rs.getString("hash");
                // commit the transaction
                conn.commit();
            }

            LOG.info(query_hash_stmt.toString());
            LOG.info(String.format("R22: hash = %s", rs_hash));
        }

        rs.close();

        return null;
    }
}


