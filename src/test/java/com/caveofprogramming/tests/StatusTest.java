package com.caveofprogramming.tests;



import com.caveofprogramming.model.StatusUpdate;
import com.caveofprogramming.model.StatusUpdateDao;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StatusTest {

    @Autowired
    private StatusUpdateDao statusUpdateDao;

    @Test
    public void testSave(){
        StatusUpdate statusUpdate = new StatusUpdate("This is a test status update");
        statusUpdateDao.save(statusUpdate);
        Assert.assertNotNull("Non-null ID", statusUpdate.getId());
        Assert.assertNotNull("Non-null Date", statusUpdate.getAdded());
        StatusUpdate retrieved = statusUpdateDao.findById(statusUpdate.getId()).get();
        Assert.assertEquals("Matching StatusUpdate", statusUpdate, retrieved);
    }

    public void testFindLatest(){
        Calendar calendar = Calendar.getInstance();

        StatusUpdate lastStatusUpdate = null;

        for(int i=0; i<10; i++){
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            StatusUpdate statusUpdate = new StatusUpdate("Status update " + i, calendar.getTime());
            statusUpdateDao.save(statusUpdate);
            lastStatusUpdate = statusUpdate;
        }
        StatusUpdate retrieved = statusUpdateDao.findFirstByOrderByAddedDesc();
        Assert.assertEquals("Latest status update", lastStatusUpdate, retrieved);
    }
}
