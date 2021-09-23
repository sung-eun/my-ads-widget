//
//  CircleImage.swift
//  myads
//
//  Created by sung-eun on 2021/09/23.
//  Copyright © 2021 essie-cho. All rights reserved.
//

import SwiftUI
import UIKit

struct CircleImage: View {
    @ObservedObject var imageLoader: ImageLoader
    @State var image: UIImage = UIImage()
    
    init(imageUrl: String) {
        imageLoader = ImageLoader(urlString: imageUrl)
    }
    
    var body: some View {
        Image(uiImage: image)
            .resizable()
            .aspectRatio(contentMode: .fill)
            .frame(width: 32, height: 32, alignment: .center)
            .clipShape(Circle())
            .overlay(Circle()
                        .stroke(Color("Green200"), lineWidth: 1.5)
                        .foregroundColor(Color("Gray200")))
            .onReceive(imageLoader.didChange) { data in
                self.image = UIImage(data: data) ?? UIImage()
            }
    }
}

struct CircleImage_Previews: PreviewProvider {
    static var previews: some View {
        CircleImage(imageUrl: "https://www.fujifilm.com/products/digital_cameras/x/fujifilm_x_t1/sample_images/img/index/pic_01.jpg")
    }
}
