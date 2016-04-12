package il.ac.shenkar.tasking.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Objectify;

@Api(
        name = "taskingapi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.tasking.shenkar.ac.il",
                ownerName = "backend.tasking.shenkar.ac.il",
                packagePath="taskingapi"
        )
)
public class TasKingAPI {

    public Employee addEmployee(Employee employee){
        return null;
    }

    public Employee removeEmployee(Employee employee){
        return null;
    }

    public Employee getEmployees(){
        return null;
    }

    public Employee getEmployee(String id){
        return null;
    }
}
