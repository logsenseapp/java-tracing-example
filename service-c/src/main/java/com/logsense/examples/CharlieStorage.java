package com.logsense.examples;
import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.utils.UUIDs;



public class CharlieStorage {

    public static class CassandraStateListener implements Host.StateListener {
        @Override
        public void onAdd(Host host) {

        }

        @Override
        public void onUp(Host host) {

        }

        @Override
        public void onDown(Host host) {

        }

        @Override
        public void onRemove(Host host) {

        }

//        @Override
//        public void onRegister(Cluster cluster) {
//
//        }
//
//        @Override
//        public void onUnregister(Cluster cluster) {
//
//        }

        @Override
        public void onSuspected(Host host) {

        }
    }

    private Session session;
    private Cluster cluster;

    public CharlieStorage() {
        open();
        createKeyspace("charlie", "SimpleStrategy", 1);
        useKeyspace("charlie");
        createTable();
    }

    private void createKeyspace(String keyspaceName, String replicatioonStrategy, int numberOfReplicas) {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append(keyspaceName).append(" WITH replication = {").append("'class':'").append(replicatioonStrategy).append("','replication_factor':").append(numberOfReplicas).append("};");

        final String query = sb.toString();

        session.execute(query);
    }

    private void useKeyspace(String keyspace) {
        session.execute("USE " + keyspace);
    }

    private void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS history ")
                .append("(").append("id uuid PRIMARY KEY, ").append("req_id text,").append("req_payload text,").append("ts int);");

        final String query = sb.toString();
        session.execute(query);
    }

    public void insertEntry(String requestId, String requestPayload) {
        StringBuilder sb = new StringBuilder("INSERT INTO history ").append("(id, req_id, req_payload, ts) ")
                .append("VALUES (")
                .append(UUIDs.timeBased()).append(", '")
                .append(requestId).append("', '")
                .append(requestPayload).append("', ")
                .append(System.currentTimeMillis()/1000).append(");");

        final String query = sb.toString();
        session.execute(query);
    }

    private void open() {
        Cluster defaultCassandraCluster = new Cluster.Builder()
                .withProtocolVersion(ProtocolVersion.V3)
                .withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
                .addContactPoint("localhost")
                .build();

        CassandraStateListener stateListener = new CassandraStateListener();
        this.cluster = defaultCassandraCluster.register(stateListener);
        session = cluster.connect();
    }

    public void close() {
        this.session.close();
        this.cluster.close();
    }

}
