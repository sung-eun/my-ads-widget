//
//  CircleImage.swift
//  myads
//
//  Created by sung-eun on 2021/09/23.
//  Copyright © 2021 essie-cho. All rights reserved.
//

import SwiftUI
import UIKit

struct CircleRemoteImage: View {
    @ObservedObject var imageLoader: ImageLoader
    @State var image: UIImage = UIImage()
    
    let borderColor: Color
    
    init(imageUrl: String, borderColor: Color = Color("Green200")) {
        imageLoader = ImageLoader(urlString: imageUrl)
        self.borderColor = borderColor
    }
    
    var body: some View {
        ZStack{
            Circle().foregroundColor(Color("Gray200"))
                .frame(width: 32, height: 32, alignment: .center)
            Image(uiImage: image)
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: 32, height: 32, alignment: .center)
                .clipShape(Circle())
                .overlay(Circle().stroke(borderColor, lineWidth: 1.5))
                .onReceive(imageLoader.didChange) { data in
                    self.image = UIImage(data: data) ?? UIImage()
                }
        }
    }
}

struct CircleResourceImage: View {
    let image: UIImage!
    let borderColor: Color
    
    init (image: UIImage!, borderColor: Color = Color("Green200")) {
        self.image = image
        self.borderColor = borderColor
    }
    
    var body: some View {
        ZStack{
            Circle().foregroundColor(Color("Gray200"))
                .frame(width: 32, height: 32, alignment: .center)
            Image(uiImage: image)
                .aspectRatio(contentMode: .fit)
                .frame(width: 32, height: 32, alignment: .center)
                .clipShape(Circle())
                .overlay(Circle().stroke(borderColor, lineWidth: 1.5))
        }
    }
}

struct CircleImage_Previews: PreviewProvider {
    static var previews: some View {
//        CircleRemoteImage(imageUrl: "https://www.fujifilm.com/products/digital_cameras/x/fujifilm_x_t1/sample_images/img/index/pic_01.jpg")
        CircleResourceImage(image: UIImage(named: "IconGhost"))
    }
}
