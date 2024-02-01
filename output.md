#[[ #Comparison result ]]#


#[[##]]# Compared artifact $report.baseArtifact to artifact $report.comparedArtifact on $comparisonDate

Found $report.differences.size issues : 

#foreach ($owner in $report.sortedIssueOwners)  
**owner** : $owner

    #foreach ($dif in $report.differencesPerOwner.get( $owner)) 
      
        issue : $dif.issue
        rating : $dif.rating
        
        #if ($dif.base)      
        base:
            $dif.base
        #end
        
        #if ($dif.missingInBase)
          missing : 
         #foreach ($miss in $dif.missingInBase)          
                $miss
          #end
         #end
        
        #if ($dif.other)      
        other:
            $dif.other
        #end
        
        #if ($dif.missingInBase)
          surplus : 
         #foreach ($surp in $dif.surplusInOther          
                $surp
          #end
         #end
                
    #end

#end