package es.regueiro.easyrepair.login;

import java.util.List;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.MultiFileSystem;
import org.openide.filesystems.XMLFileSystem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = FileSystem.class)
public class PermissionGroupFileSystem extends MultiFileSystem {

   public PermissionGroupFileSystem() {
      setPropagateMasks(true);
   }

   public static PermissionGroupFileSystem getDefault() {
      return Lookup.getDefault().lookup(PermissionGroupFileSystem.class);
   }

   public void setPermissionGroups(List<PermissionGroup> groups) 
           throws Exception {
      FileSystem[] fileSystems = new FileSystem[groups.size()];
      for (int idx = 0; idx < fileSystems.length; idx++) {
         fileSystems[idx] = 
                 new XMLFileSystem(groups.get(idx).getConfig());
      }
      setDelegates(fileSystems);
   }
}
