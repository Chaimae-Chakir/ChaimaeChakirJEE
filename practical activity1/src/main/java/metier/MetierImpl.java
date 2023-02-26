package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class MetierImpl implements IMetier{
    @Autowired
    private IDao dao;
    @Override
    public Date calcul() {
        Date currentDate = dao.getDate();
        long time = currentDate.getTime();
        time += (24 * 60 * 60 * 1000); // add one day in milliseconds
        return new Date(time);
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
