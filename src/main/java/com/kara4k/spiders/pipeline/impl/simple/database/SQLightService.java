package com.kara4k.spiders.pipeline.impl.simple.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Aliaksei Voitsel
 */
@Service
@NoArgsConstructor
public class SQLightService<T> {
    private String dbUrl = "jdbc:sqlite:";
    private ConnectionSource source;
    private Map<Class, Dao<T, String>> daoMap;

    public SQLightService(final String dbUrl) throws SQLException {
        init(dbUrl);
    }

    public void init(final String dbUrl) throws SQLException {
        this.dbUrl = this.dbUrl.concat(dbUrl);
        source = new JdbcConnectionSource(this.dbUrl);
        daoMap = new HashMap<>();
    }

    public void add(final T t) throws SQLException {
        Optional.ofNullable(daoMap.get(t.getClass()))
            .orElseGet(provideDao(t,true))
            .create(t);
    }

    public List<T> getAll(final T t) throws SQLException {
        return Optional.ofNullable(daoMap.get(t.getClass()))
            .orElseGet(provideDao(t,false)).queryForAll();
    }

    public Dao<T, String> getDao(final T t, final boolean isCreateTableOnNonExists){
        return provideDao(t, isCreateTableOnNonExists).get();
    }

    @SuppressWarnings("unchecked")
    private Supplier<Dao<T, String>> provideDao(final T t, final boolean isCreateTableOnNonExists){
        return () -> {
            final Dao<T, String> newDao;

            try {
                if (isCreateTableOnNonExists){
                    TableUtils.createTableIfNotExists(source, t.getClass());
                }
                newDao = DaoManager.createDao(source, (Class<T>) t.getClass());
            } catch (final SQLException e) {
                throw new RuntimeException("Can't create DAO");
            }

            daoMap.put(t.getClass(), newDao);

            return newDao;
        };
    }

    public void close() throws SQLException {
        source.close();
    }
}
