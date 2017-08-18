package com.olegsh.crudauto.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Oleg on 14.08.2017.
 */
@Entity(
        active = true,
        nameInDb = "VEHICLES",
        generateConstructors = true,
        generateGettersSetters = true
)
public class Vehicle {
    @Id
    private Long id;

    private Long driverId;

    @NotNull
    private String number;

    @NotNull
    private String name;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 900796925)
private transient VehicleDao myDao;
@Generated(hash = 1333536258)
public Vehicle(Long id, Long driverId, @NotNull String number, @NotNull String name) {
    this.id = id;
    this.driverId = driverId;
    this.number = number;
    this.name = name;
}
@Generated(hash = 2006430483)
public Vehicle() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public Long getDriverId() {
    return this.driverId;
}
public void setDriverId(Long driverId) {
    this.driverId = driverId;
}
public String getNumber() {
    return this.number;
}
public void setNumber(String number) {
    this.number = number;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
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
@Generated(hash = 1588469812)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getVehicleDao() : null;
}

}
