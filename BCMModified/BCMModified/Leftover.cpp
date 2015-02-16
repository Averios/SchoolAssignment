////Selecting candidate from every row
//for(int i = 0; i < row; i++){
//    //Sort the vector
//    if(up){
//        sort(PointingY[i].begin(),PointingY[i].end(), sorter);
//    }
//    else{
//        sort(PointingY[i].begin(), PointingY[i].end(), backsort);
//    }
//    counter = 1;
//    //Select the candidate(2 optimum value)
//    optimum = PointingY[i][0].first;
//    for(int j = 0; j < column; j++){

//        if(PointingY[i][j].first == optimum){
//            PointingY[i][j].second->active++;
//            tempColumn[PointingY[i][j].second->x]++;
//        }
//        else{
//            counter++;
//            if(counter < 3){
//                optimum = PointingY[i][j].first;
//                PointingY[i][j].second->active++;
//                tempColumn[PointingY[i][j].second->x]++;
//            }
//            else break;
//        }
//    }

//}

////electing candidate from every column
//int stop;
//for(int i = 0; i < column; i++){
//    if(tempColumn[i] == 0) continue;
//    if(up){
//        sort(PointingX[i].begin(),PointingX[i].end(), sorter);
//    }
//    else {
//        sort(PointingX[i].begin(), PointingX[i].end(), backsort);
//    }
//    counter = 0;
//    for(int j = 0; j < row; j++){
//        if(PointingX[i][j].second->active == 1){
//            optimum = PointingX[i][j].first;
////                PointingX[i][j].second->active++;
//            stop = j;
//            counter++;
//            break;
//        }
//    }

//    for(int j = stop; j < row; j++){
//        if(PointingX[i][j].second->active == 1){
//            if(PointingX[i][j].first == optimum){

//                //Update the record of row and column
//                rowCount[PointingX[i][j].second->y]++;
//                columnCount[PointingX[i][j].second->x]++;

//                PointingX[i][j].second->active++;
//            }
//            else{
//                counter++;
//                if(counter < 3){
//                    PointingX[i][j].second->active++;
//                    optimum = PointingX[i][j].first;

//                    //Update the record of row and column
//                    rowCount[PointingX[i][j].second->y]++;
//                    columnCount[PointingX[i][j].second->x]++;


//                }
//                else break;
//            }
//        }
//        else continue;
//    }
//}
