package com.olegsh.crudauto.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity (
        active = true,
        nameInDb = "DRIVERS",
        generateConstructors = true,
        generateGettersSetters = true
)
public class Driver {
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String category;

    @ToMany(referencedJoinProperty = "driverId")
    private List<Vehicle> vehicles;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1290256490)
private transient DriverDao myDao;

@Generated(hash = 878442659)
public Driver(Long id, @NotNull String name, @NotNull String category) {
    this.id = id;
    this.name = name;
    this.category = category;
}

@Generated(hash = 911393595)
public Driver() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return this.name;
}

public void setName(String name) {
    this.name = name;
}

public String getCategory() {
    return this.category;
}

public void setCategory(String category) {
    this.category = category;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1695379811)
public List<Vehicle> getVehicles() {
    if (vehicles == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        VehicleDao targetDao = daoSession.getVehicleDao();
        List<Vehicle> vehiclesNew = targetDao._queryDriver_Vehicles(id);
        synchronized (this) {
            if (vehicles == null) {
                vehicles = vehiclesNew;
            }
        }
    }
    return vehicles;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1696362987)
public synchronized void resetVehicles() {
    vehicles = null;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 2144157779)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getDriverDao() : null;
}
}
