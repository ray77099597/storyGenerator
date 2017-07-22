package generator;

public class HumorConceptGenerator {
	String concepts[];
	
	public String[] generateConcept(String cpt, String state){
		
//		List<String> nextStates = Parameter.nextStates.get(state);
//		ArrayList<String> nextc = new ArrayList<String>();
//		ArrayList<String> nexts = new ArrayList<String>();
//		ResultSet rs;
//		int[] numArr = new int[Parameter.fabulaElement.length];
//		String nextState = null;
//		String nSConcept = cpt;
//		String prevState = state;
//		String nSName = null;
//		String pSName = null;
//		String tableName = null;
//		
//		StringBuffer selectSQL = new StringBuffer();
//		StringBuffer countSQL = new StringBuffer();
//		
//		for (int i = 0; i < nextStates.size(); i++) {
//			if (i != 0)
//				countSQL.append(" union ");
//			nextState = nextStates.get(i);
//			nSName = Utility.stateName(nextState);
//			pSName = Utility.stateName(prevState);
//			if (nextState.equals(prevState)) {
//				pSName = pSName + "1";
//				nSName = nSName + "2";
//			}
//			
//			tableName = prevState + "_" + nextState;
//			countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.database).append(".").append(tableName).append(" where ").append(pSName).append(" = '").append(nSConcept).append("'");
//		}
//		try {
//			rs = Parameter.stm.executeQuery(countSQL.toString());
//			countSQL.delete(0, countSQL.length());
//			rs.beforeFirst();
//			int tempCnt = 0;
//			while (rs.next()) {
//				numArr[tempCnt++] = rs.getInt("count(*)");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		int totalrsNum = 0;
//		for (int i = 0; i < nextStates.size(); i++) {
//			totalrsNum += numArr[i];
//		}
//		if (totalrsNum == 0) {
//			return null;
//		}
//		String nst;
//		for(int i=0;i<totalrsNum; i++){
//			for(int j=0; j<nextStates.size(); j++){
//				nextState = nextStates.get(j);
//				pSName = Utility.stateName(prevState);
//				nSName = Utility.stateName(nextState);
//				if (nextState.equals(prevState)) {
//					pSName = pSName + "1";
//					nSName = nSName + "2";
//				}
//				tableName = prevState + "_" + nextState;
//				selectSQL.append("select ").append("* from ").append(Parameter.database).append(".").append(tableName).append(" where ").append(pSName).append(" = '").append(nSConcept).append("'");
//				try {
//					rs = Parameter.stm.executeQuery(selectSQL.toString());
//					selectSQL.delete(0, selectSQL.length());
//					rs.beforeFirst();
//					while(rs.next()){
//						nextc.add(rs.getString(nSName));
//						nexts.add(prevState);
//						System.out.println(rs.getString(nSName)+" "+prevState);
//					}
//					rs.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//		
//		for(int k=0; k<nextc.size(); k++){
//			nextStates = Parameter.nextStates.get(nexts.get(k));
//			nSConcept = nextc.get(k);
//			for (int i = 0; i < nextStates.size(); i++) {
//				if (i != 0)
//					countSQL.append(" union ");
//				nextState = nextStates.get(i);
//				nSName = Utility.stateName(nextState);
//				pSName = Utility.stateName(prevState);
//				if (nextState.equals(prevState)) {
//					pSName = pSName + "1";
//					nSName = nSName + "2";
//				}
//				
//				tableName = prevState + "_" + nextState;
//				countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.database).append(".").append(tableName).append(" where ").append(pSName).append(" = '").append(nSConcept).append("'");
//			}
//			try {
//				rs = Parameter.stm.executeQuery(countSQL.toString());
//				countSQL.delete(0, countSQL.length());
//				rs.beforeFirst();
//				int tempCnt = 0;
//				while (rs.next()) {
//					numArr[tempCnt++] = rs.getInt("count(*)");
//				}
//				rs.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			totalrsNum = 0;
//			for (int i = 0; i < nextStates.size(); i++) {
//				totalrsNum += numArr[i];
//			}
//			if (totalrsNum == 0) {
//				return null;
//			}
//			for(int i=0;i<totalrsNum; i++){
//				for(int j=0; j<nextStates.size(); j++){
//					nextState = nextStates.get(j);
//					pSName = Utility.stateName(prevState);
//					nSName = Utility.stateName(nextState);
//					nst = nSName;
//					if (nextState.equals(prevState)) {
//						pSName = pSName + "1";
//						nSName = nSName + "2";
//					}
//					tableName = prevState + "_" + nextState;
//					selectSQL.append("select ").append("* from ").append(Parameter.database).append(".").append(tableName).append(" where ").append(pSName).append(" = '").append(nSConcept).append("'");
//					try {
//						rs = Parameter.stm.executeQuery(selectSQL.toString());
//						selectSQL.delete(0, selectSQL.length());
//						rs.beforeFirst();
//						while(rs.next()){
//							nextc.add(rs.getString(nSName));
//							nexts.add(nst);
//							System.out.println(cpt+"->"+nSConcept+"->"+rs.getString(nSName));
//						}
//						rs.close();
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//			}
//		}
		
		
		return concepts;
	}
	

	
}
